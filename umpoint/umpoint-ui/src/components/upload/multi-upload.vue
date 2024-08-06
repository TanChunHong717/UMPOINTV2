<template>
  <div>
    <el-upload
      ref="upload"
      v-model:file-list="fileList"
      :action="actionUrl"
      :http-request="uploadFile"
      :before-upload="beforeUpload"
      :headers="headers"
      :data="uploadData"
      :with-credentials="false"
      list-type="picture"
      auto-upload
    >
      <el-button :type="buttonType">{{ buttonText }}</el-button>
    </el-upload>
  </div>
</template>
<script>
import axios from 'axios';
import baseService from "@/service/baseService.ts";
import {ButtonType} from "@/constants/enum.js"
import {uuid} from "vue-uuid";

export default {
  props: {
    url: {
      type: Array,
      default: []
    },
    buttonText: {
      type: String,
      default: "Select upload file"
    },
    buttonType: {
      type: ButtonType,
      default: "primary"
    }
  },
  watch: {
    url(newVal, oldVal) {
      for (let i = 0; i < newVal.length; i++) {
        if (newVal[i].trim().length > 0) {
          const parts = newVal[i].split('/');
          const fileName = parts[parts.length-1].split('_')[1];
          this.fileList.push({name: fileName, url: newVal[i]});
        }
      }
    }
  },
  data() {
    return {
      actionUrl: '',
      sasToken: '',
      containerName: 'upload',
      newFileName: '',
      fileList: [],
      uploadPercentage: 0,
      headers: {},
      uploadData: {}
    };
  },
  methods: {
    async getSasToken() {
      await baseService.get("sas")
        .then((res) => {
          this.sasToken = res;
          this.actionUrl = `https://mallstore.blob.core.windows.net/${this.containerName}?${this.sasToken}`;
        })
        .catch((error) => {
          console.error('Error fetching SAS token:', error);
        });
    },
    async beforeUpload(file) {
      await this.getSasToken();
      this.uploadData = {
        'x-ms-blob-type': 'BlockBlob',
      };
      this.headers = {
        'x-ms-blob-type': 'BlockBlob',
      };
      this.newFileName = uuid.v4() + '_' + file.name;
      this.actionUrl = `https://mallstore.blob.core.windows.net/${this.containerName}/${this.newFileName}?${this.sasToken}`;
      return true;
    },
    uploadFile(options) {
      const formData = new FormData();
      formData.append('file', options.file);
      axios.put(options.action, options.file, {
        headers: this.headers,
        onUploadProgress: progressEvent => {
          this.uploadPercentage = Math.round((progressEvent.loaded * 100) / progressEvent.total);
        }
      }).then(response => {
        this.uploadPercentage = 0;
        this.$emit('update', this.fileList.map((file) => {file.url}));
      }).catch(error => {
        this.uploadPercentage = 0;
        console.error('Upload error:', error);
      });
    }
  },
  mounted() {
    this.getSasToken();
  }
};
</script>
