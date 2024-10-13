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
      :limit="maxCount"
      :with-credentials="false"
      :on-remove="onRemove"
      :on-exceed="onExceed"
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
      type: String,
      default: "primary"
    },
    maxCount: {
      type: Number,
      default: 30
    }
  },
  watch: {
    url: {
      handler(newVal, oldVal) {
        this.fileList = [];
        this.uploadFileList = [];
        for (let i = 0; i < this.url.length; i++) {
          this.fileList.push({
            id: this.url[i].id,
            name: this.url[i].url.substring(this.url[i].url.lastIndexOf('_')+1, this.url[i].url.length-1),
            url: this.url[i].url
          })
          this.uploadFileList.push({id: this.url[i].id, url: this.url[i].url})
        }
      },
      deep: true
    }
  },
  data() {
    return {
      actionUrl: '',
      sasToken: '',
      containerName: 'upload',
      newFileName: '',
      fileList: [],
      uploadFileList: [],
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
      }).then(response => {
        this.$message({
          message: "Upload successfully",
          type: "success",
          duration: 1000
        });
        this.uploadFileList.push({id:undefined, imageUrl:`https://mallstore.blob.core.windows.net/${this.containerName}/${this.newFileName}`});
        this.$emit('upload', this.uploadFileList);
      }).catch(error => {
      });
    },
    onRemove(uploadFile, uploadFiles) {
      this.uploadFileList = uploadFiles.map((file) => {
        return {
          id: file.id,
          imageUrl: file.url
        }
      });
      this.$emit('upload', this.uploadFileList);
    },
    onExceed(files, fileList) {
      this.$message({
        message: "Can only upload " + this.maxCount + " document(s).",
        type: "warning",
        duration: 1000
      });
    }
  },
  activated() {
    this.getSasToken();
  }
};
</script>
