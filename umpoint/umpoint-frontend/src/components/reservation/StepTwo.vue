<script setup>
import { mdiToolboxOutline, mdiMessageBulleted, mdiTrayArrowUp } from "@mdi/js";
import { reactive, useTemplateRef } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import EventInfo from "./EventInfo.vue";
import { uploadFile as uploadFileApi } from "@/helpers/api-upload";

const props = defineProps(["formData", "facilityInfo"]);
const emit = defineEmits(["nextStep", "previousStep"]);
defineOptions({
    inheritAttrs: false,
});

const formData = reactive({
    equipments: null,
    approvalDocuments: [],
    supportingDocuments: [],
});

/**
 * STEP 2: ADDITIONAL INFORMATION
 * */
const formNode = useTemplateRef("formNode");

async function uploadFile(options) {
    try {
        return await uploadFileApi(options.file, {
            onUploadProgress: (progressEvent) => {
                if (progressEvent.lengthComputable) {
                    let percentCompleted = Math.round(
                        (progressEvent.loaded * 100) / progressEvent.total
                    );
                    options.onProgress({ percent: percentCompleted });
                }
            },
        });
    } catch (error) {
        return options.onError(error);
    }
}

// confirm before cancel
const beforeRemove = (file) => {
    return ElMessageBox.confirm(`Are you sure to remove ${file.name}?`).then(
        () => true,
        () => false
    );
};

// form submit
async function returnFormInfo(formEl) {
    if (!formEl) return;
    await formEl.validate((valid, fields) => {
        if (valid) {
            emit("nextStep", formData);
        } else {
            console.log("error submit!", fields);
        }
    });
}
</script>

<template>
    <EventInfo :formData="props.formData" v-if="props.formData" />

    <el-form ref="formNode" label-position="top" :model="formData">
        <el-divider content-position="left">
            <h2>Facility Details</h2>
        </el-divider>

        <el-card class="information-card" shadow="never">
            <el-text class="info-item" size="small">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiToolboxOutline"
                />
                {{ props.facilityInfo.facilities }}
            </el-text>
            <el-text class="info-item" size="small">
                <svg-icon
                    class="info-icon"
                    type="mdi"
                    :path="mdiMessageBulleted"
                />
                UNTUK KETERANGAN LANJUT, SILA HUBUNGI PUAN NORFAIZAH MOHD SUPI,
                NOMBOR TELEFON: 03-7967 52349 @ E-MAIL: faiza_21@um.edu.my ATAU
                EN. AFANDI AYUB TELEFON 03-7967 5151 @ EMAIL: a.afandi@um.edu.my
                Sila patuhi panduan di bawah: 1) Dewan/ruang hanya akan dibuka
                selewat-lewatnya 15-30 minit sebelum majlis bermula; 2) Segala
                sampah-sarap hendaklah dibersihkan dan dibawa keluar oleh
                penyewa (tiada perkhidmatan pembersihan disediakan oleh pihak
                fakulti); 3) Sila pastikan ruang/peralatan/kerusi/meja berada
                dalam keadaan asal; 4) Peralatan/Perkakasan yang disediakan
                adalah seperti yang tertera di dalam sistem UMPoint. Tiada
                tambahan peralatan/perkakasan pada saat akhir; 5) Dewan/ruang
                akan ditutup selewat-lewatnya 15-30 minit selepas masa tempahan.
                Sebarang kelewatan yang berpunca dari penyewa boleh dikenakan
                caj tambahan mengikut kadar yang telah ditetapkan (sewa/bayaran
                staf teknikal). Sila patuhi tempoh masa yang telah ditetapkan;
                6) Pihak kami berhak menukar lokasi tertakluk kepada keadaan
                ruang yang disewa (jika melibatkan keselesaan/keselamatan
                pengguna); 7) Sebarang kerosakan yang berpunca dari pihak
                penyewa, adalah di bawah tanggungjawab penyewa.
            </el-text>
        </el-card>

        <el-form-item label="Equipments" prop="equipments">
            <el-select
                disabled
                v-model="formData.equipments"
                placeholder="No equipments available for selection"
            ></el-select>
        </el-form-item>

        <el-divider content-position="left">
            <h2>Supporting Documents</h2>
        </el-divider>

        <el-alert type="info" show-icon :closable="false">
            <ul>
                <li>
                    Please upload supporting documents to be eligible for
                    discount.
                </li>
                <li>Please limit your filename to 50 characters.</li>
                <li>Allowed document type: PDF</li>
            </ul>
        </el-alert>

        <el-row :gutter="8">
            <el-col :xs="24" :sm="12">
                <el-form-item>
                    <template #label>
                        Approval Documents from HEP
                        <el-text size="small">(optional)</el-text>
                    </template>
                    <el-upload
                        v-model:file-list="formData.approvalDocuments"
                        :http-request="uploadFile"
                        :before-remove="beforeRemove"
                        multiple
                        :limit="3"
                        :on-exceed="
                            (files) => {
                                files;
                            }
                        "
                        style="width: 100%"
                    >
                        <template #trigger>
                            <el-button plain>
                                <template #icon>
                                    <svg-icon
                                        type="mdi"
                                        :path="mdiTrayArrowUp"
                                    ></svg-icon>
                                </template>
                                Click to upload
                            </el-button>
                        </template>
                        <template #tip>
                            <div class="el-upload__tip">
                                Maximum 3 files, only PDF is allowed
                            </div>
                        </template>
                    </el-upload>
                </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12">
                <el-form-item>
                    <template #label>
                        Supporting Documents
                        <el-text size="small">(optional)</el-text>
                    </template>
                    <el-upload
                        v-model:file-list="formData.supportingDocuments"
                        action="#"
                        multiple
                        :before-remove="beforeRemove"
                        :auto-upload="false"
                        :limit="3"
                        :on-exceed="
                            (files) => {
                                files;
                            }
                        "
                        style="width: 100%"
                    >
                        <template #trigger>
                            <el-button plain>
                                <template #icon>
                                    <svg-icon
                                        type="mdi"
                                        :path="mdiTrayArrowUp"
                                    ></svg-icon>
                                </template>
                                Click to upload
                            </el-button>
                        </template>
                        <template #tip>
                            <div class="el-upload__tip">
                                Maximum 3 files, only PDF is allowed
                            </div>
                        </template>
                    </el-upload>
                </el-form-item>
            </el-col>
        </el-row>

        <div class="end-buttons">
            <el-button plain @click="emit('previousStep')">
                Previous
            </el-button>
            <el-button type="primary" @click="returnFormInfo(formNode)">
                Next
            </el-button>
        </div>
    </el-form>
</template>

<style scoped>
.el-upload__tip {
    margin-top: 0;
    line-height: 1.2;
}
</style>
