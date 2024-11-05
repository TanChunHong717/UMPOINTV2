import api from "@/utils/api";
import { getUuid } from "@/utils/utils";

const headers = {
    "x-ms-blob-type": "BlockBlob",
};
const containerName = "upload";

async function uploadFile(file) {
    console.log("uploading file", file);

    let newFileName = generateNewFileName(file);
    let url = await buildSasUrl(newFileName);

    const formData = new FormData();
    formData.append("file", file);

    await api.put(url, file.blob, {
        headers,
    });
    return {
        name: newFileName,
        type: file.type,
        url: `https://mallstore.blob.core.windows.net/${containerName}/${newFileName}`,
    };
}

async function buildSasUrl(fileName) {
    let sasToken = await getSasToken();
    console.log("sasToken", sasToken);
    return `https://mallstore.blob.core.windows.net/${containerName}/${fileName}?${sasToken}`;
}

async function getSasToken() {
    let response = await api.get("sas");
    console.log("response sas", response);
    return response.data;
}

function generateNewFileName(file) {
    return `${getUuid()}-${file.name}.${file.extension}`;
}

export { uploadFile, getSasToken, buildSasUrl, generateNewFileName };
