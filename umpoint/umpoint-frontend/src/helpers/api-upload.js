import api from "@/utils/api";
import { getUuid } from "@/utils/utils";

const headers = {
    "x-ms-blob-type": "BlockBlob",
};
const containerName = "upload";

// upload file to azure blob storage
export async function uploadFile(file, config = {}) {
    // file: File object
    // {name, type, item}
    let newFileName = generateNewFileName(file.name);
    let url = await buildSasUrl(newFileName);

    const formData = new FormData();
    formData.append("file", file);

    await api.put(url, file.item, {
        headers,
        ...config,
    });
    return {
        name: newFileName,
        type: file.type,
        url: `https://mallstore.blob.core.windows.net/${containerName}/${newFileName}`,
    };
}

export async function buildSasUrl(fileName) {
    let sasToken = await getSasToken();
    return `https://mallstore.blob.core.windows.net/${containerName}/${fileName}?${sasToken}`;
}

export async function getSasToken() {
    let response = await api.get("sas");
    return response.data;
}

export function generateNewFileName(filename) {
    return `${getUuid()}-${filename}`;
}