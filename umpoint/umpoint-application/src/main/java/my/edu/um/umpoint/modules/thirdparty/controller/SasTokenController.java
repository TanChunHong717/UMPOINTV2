package my.edu.um.umpoint.modules.thirdparty.controller;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.SasProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@ConfigurationProperties(prefix = "azure.storage")
@RestController
public class SasTokenController {

    @Autowired
    private BlobServiceClient blobServiceClient;

    private String container;

    @GetMapping("sas")
    public String getSasToken(){
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(container);

        BlobSasPermission permission = new BlobSasPermission()
                .setWritePermission(true);

        OffsetDateTime expiryTime = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1);

        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permission)
                .setProtocol(SasProtocol.HTTPS_ONLY)
                .setStartTime(OffsetDateTime.now(ZoneOffset.UTC))
                .setExpiryTime(OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(30));

        return containerClient.generateSas(sasValues);
    }
}
