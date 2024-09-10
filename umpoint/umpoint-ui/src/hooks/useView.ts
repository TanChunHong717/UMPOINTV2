import app from "@/constants/app";
import { EMitt, EThemeSetting } from "@/constants/enum";
import { IObject, IViewHooks, IViewHooksOptions } from "@/types/interface";
import { registerDynamicToRouterAndNext } from "@/router";
import baseService from "@/service/baseService";
import { getToken } from "@/utils/cache";
import emits from "@/utils/emits";
import { getThemeConfigCacheByKey } from "@/utils/theme";
import { checkPermission, getDictLabel } from "@/utils/utils";
import qs from "qs";
import { onActivated, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useAppStore } from "@/store";
import { ElMessage, ElMessageBox } from "element-plus";

/**
 * Common view business logic (crud)
 * @param props custom state
 * @returns 返回响应式自定义state和通用方法
 */
const useView = (props: IViewHooksOptions | IObject): IViewHooks => {
  const router = useRouter();
  const route = useRoute();
  const store = useAppStore();
  const defaultOptions: IViewHooksOptions = {
    createdIsNeed: true,
    activatedIsNeed: false,
    getDataListURL: "",
    getDataListIsPage: false,
    deleteURL: "",
    deleteIsBatch: false,
    deleteIsBatchKey: "id",
    deleteMessage: "Confrim to delete?",
    exportURL: "",
    dataForm: {},
    dataList: [],
    order: "",
    orderField: "",
    page: 1,
    limit: 10,
    total: 0,
    dataListLoading: false,
    dataListSelections: [],
    elTable: {}
  };
  const mergeDefaultStateToPageState = (options: IObject, props: IObject): IViewHooksOptions => {
    for (const key in options) {
      if (!Object.getOwnPropertyDescriptor(props, key)) {
        props[key] = options[key];
      }
    }
    return props;
  };
  const state = mergeDefaultStateToPageState(defaultOptions, props);
  onMounted(() => {
    if (state.createdIsNeed && !state.activatedIsNeed) {
      viewFns.query();
    }
  });
  onActivated(() => {
    if (store.state.closedTabs.includes(store.state.activeTabName)) {

      const closedTabs = store.state.closedTabs;
      store.updateState({
        closedTabs: closedTabs.filter((x: string) => x !== store.state.activeTabName)
      });
      emits.emit(EMitt.OnReloadTabPage);
    }

    if (state.activatedIsNeed) {
      viewFns.query();
    }
  });
  const rejectFns = {
    hasPermission(key: string) {
      return checkPermission(store.state.permissions as string[], key);
    },
    getDictLabel(dictType: string, dictValue: number) {
      return getDictLabel(store.state.dicts, dictType, dictValue);
    }
  };
  const viewFns = {
    // Query
    query() {
      if (!state.getDataListURL) {
        return;
      }
      state.dataListLoading = true;
      baseService
        .get(state.getDataListURL, {
          order: state.order,
          orderField: state.orderField,
          page: state.getDataListIsPage ? state.page : null,
          limit: state.getDataListIsPage ? state.limit : null,
          ...state.dataForm
        })
        .then((res) => {
          state.dataListLoading = false;
          state.dataList = state.getDataListIsPage ? res.data.list : res.data;
          state.total = state.getDataListIsPage ? res.data.total : 0;
        })
        .catch(() => {
          state.dataListLoading = false;
        });
    },
    // Multi select
    dataListSelectionChangeHandle(val: IObject[]) {
      state.dataListSelections = val;
    },
    // Sort
    dataListSortChangeHandle(data: IObject) {
      if (!data.order || !data.prop) {
        state.order = "";
        state.orderField = "";
        return false;
      }
      state.order = data.order.replace(/ending$/, "");
      state.orderField = data.prop.replace(/([A-Z])/g, "_$1").toLowerCase();
      viewFns.query();
    },
    // Page size change handler
    pageSizeChangeHandle(val: number) {
      state.page = 1;
      state.limit = val;
      viewFns.query();
    },
    // Current page change handler
    pageCurrentChangeHandle(val: number) {
      state.page = val;
      viewFns.query();
    },
    // Search
    getDataList() {
      state.page = 1;
      viewFns.query();
    },
    // Delete
    deleteHandle(id?: string): Promise<any> {
      return new Promise((resolve, reject) => {
        if (
          state.deleteIsBatch &&
          !id &&
          state.dataListSelections &&
          state.dataListSelections.length <= 0
        ) {
          ElMessage.warning({
            message: "Please select data",
            duration: 500
          });
          return;
        }
        ElMessageBox.confirm(state.deleteMessage, "Hint", {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning"
        })
          .then(() => {
            baseService
              .delete(
                `${state.deleteURL}${state.deleteIsBatch ? "" : "/" + id}`,
                state.deleteIsBatch
                  ? id
                    ? [id]
                    : state.dataListSelections
                    ? state.dataListSelections.map(
                        (item: IObject) => state.deleteIsBatchKey && item[state.deleteIsBatchKey]
                      )
                    : {}
                  : {}
              )
              .then((res) => {
                ElMessage.success({
                  message: "Success",
                  duration: 500,
                  onClose: () => {
                    viewFns.query();
                    resolve(true);
                  }
                });
              });
          })
          .catch(() => {
            //
          });
      });
    },
    // Export
    exportHandle() {
      window.location.href = `${app.api}${state.exportURL}?${qs.stringify({
        ...state.dataForm,
        token: getToken()
      })}`;
      // baseService.download(state.exportURL, { ...state.dataForm, token: getToken() });
    },
    // Close current tab
    closeCurrentTab() {
      if (getThemeConfigCacheByKey(EThemeSetting.OpenTabsPage)) {
        emits.emit(EMitt.OnCloseCurrTab);
      } else {
        router.replace("/home");
      }
    },
    // Handle route flow
    handleFlowRoute(data: IObject) {
      const routeParams = {
        path: `/flow/task-form`,
        query: {
          taskId: data.taskId,
          processInstanceId: data.processInstanceId,
          processDefinitionId: data.processDefinitionId,
          showType: "taskHandle",
          _mt: `${route.meta.title} - ${data.processDefinitionName}`
        }
      };
      registerDynamicToRouterAndNext(routeParams);
    },
    // Check flow detail
    flowDetailRoute(data: IObject) {
      const routeParams = {
        path: `/flow/task-form`,
        query: {
          taskId: data.taskId,
          processInstanceId: data.processInstanceId,
          processDefinitionId: data.processDefinitionId,
          showType: "detail",
          _mt: `${route.meta.title} - ${data.processDefinitionName}`
        }
      };
      registerDynamicToRouterAndNext(routeParams);
    }
  };

  //
  return {
    ...viewFns,
    ...rejectFns
  };
};

export default useView;
