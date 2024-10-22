package my.edu.um.umpoint.modules.thirdparty.controller;

import com.azure.storage.blob.*;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class SasTokenController {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container}")
    private String containerName;

    @GetMapping("sas")
    @RequiresAuthentication
    public String generateSasToken() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        BlobContainerSasPermission permission = new BlobContainerSasPermission()
                .setReadPermission(true)
                .setReadPermission(true)
                .setCreatePermission(true)
                .setListPermission(true);

        OffsetDateTime expiryTime = OffsetDateTime.now().plusHours(1);

        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permission)
                .setProtocol(SasProtocol.HTTPS_ONLY);

        return containerClient.generateSas(sasValues);
    }
}
