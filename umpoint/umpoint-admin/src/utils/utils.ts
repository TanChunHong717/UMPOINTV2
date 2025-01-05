import { IFunction, IObject } from "@/types/interface";
import { debounce, DebouncedFunc, DebounceSettings } from "lodash";
import type { App, Plugin } from "vue";

export const getValueByKeys = (record: IObject = {}, key: string, defaultValue?: unknown): any => {
  const keys = key.split(".");
  for (let i = 0; i < keys.length; i++) {
    record = record[keys[i]] || (i === keys.length - 1 ? defaultValue : {});
  }
  return record || defaultValue;
};

export const arrayToObject = (
  rs: any[] = [],
  key: string | IFunction,
  render?: IFunction
): IObject => {
  const o: IObject = {};
  rs.forEach((x) => {
    o[typeof key === "function" ? key(x) : x[key]] = render ? render(x) : x;
  });
  return o;
};

export const arrayToKeyValueArray = (rs: any[] = [], render?: IFunction): any[] => {
  return rs.map((x) => (render ? render(x) : typeof x === "object" ? x : { label: x, value: x }));
};

export const isNullOrUndefined = (vl: unknown): boolean => {
  return vl === null || typeof vl === "undefined";
};

export const isExternalLink = (path: string): boolean => {
  return /^(https?:|\/\/|mailto:|tel:|^{{\s?ApiUrl\s?}})/.test(path);
};

export const copyToClipboard = (value: string): void => {
  const transfer = document.createElement("textarea");
  document.body.appendChild(transfer);
  transfer.value = value;
  transfer.focus();
  transfer.select();
  if (document.execCommand("copy")) {
    document.execCommand("copy");
  }
  transfer.blur();
  document.body.removeChild(transfer);
};

export const checkPermission = (permission: string[], key: string): boolean => {
  return permission.includes(key);
};

export const getUuid = (): string => {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    const r = (Math.random() * 16) | 0,
      v = c == "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
};

export const isEmail = (s: string): boolean => {
  return /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s);
};

export const isMobile = (s: string): boolean => {
  return /^1[0-9]{10}$/.test(s);
};

export const isPhone = (s: string): boolean => {
  return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s);
};

export const isURL = (s: string): boolean => {
  return /^http[s]?:\/\/.*/.test(s);
};

export const isNumber = (s: string): boolean => {
  return /^\+?[1-9][0-9]*$/.test(s);
};

export const getDictDataList = (list: IObject[], dictType?: string): IObject[] => {
  const type = list.find((element: IObject) => element.dictType === dictType);
  if (type) {
    return type.dataList;
  } else {
    return [];
  }
};

export const getDictLabel = (
  list: IObject[],
  dictType: string,
  dictValue: number
): string | number => {
  const type = list.find((element: IObject) => element.dictType === dictType);
  if (type) {
    const val = type.dataList.find((element: IObject) => element.dictValue === dictValue + "");
    if (val) {
      return val.dictLabel;
    } else {
      return dictValue;
    }
  } else {
    return dictValue;
  }
};

export const getIconList = (): string[] => {
  const rs: string[] = [];
  const list = document.querySelectorAll("svg symbol");
  for (let i = 0; i < list.length; i++) {
    rs.push(list[i].id);
  }
  return rs;
};

export const treeDataTranslate = (data: IObject[], id?: string, pid?: string): IObject[] => {
  const res: IObject[] = [];
  const temp: IObject = {};
  id = id || "id";
  pid = pid || "pid";
  for (let i = 0; i < data.length; i++) {
    temp[data[i][id]] = data[i];
  }
  for (let k = 0; k < data.length; k++) {
    if (!temp[data[k][pid]] || data[k][id] === data[k][pid]) {
      res.push(data[k]);
      continue;
    }
    if (!temp[data[k][pid]]["children"]) {
      temp[data[k][pid]]["children"] = [];
    }
    temp[data[k][pid]]["children"].push(data[k]);
    data[k]["_level"] = (temp[data[k][pid]]._level || 0) + 1;
  }
  return res;
};

export const withInstall = <T extends object>(component: T, alias?: string): T & Plugin => {
  const comp = component as any;
  comp.install = (app: App) => {
    app.component(comp.name || comp.displayName, component);
    if (alias) {
      app.config.globalProperties[alias] = component;
    }
  };
  return component as T & Plugin;
};

export const useDebounce = (
  fn: (e?: any) => any,
  wait?: number,
  options?: DebounceSettings
): DebouncedFunc<any> => {
  return debounce(fn, wait ?? 1000, {
    leading: true,
    trailing: false,
    ...options
  });
};
