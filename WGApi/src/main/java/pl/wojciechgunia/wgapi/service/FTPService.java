package pl.wojciechgunia.wgapi.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wojciechgunia.wgapi.entity.FileEntity;
import pl.wojciechgunia.wgapi.exceptions.FtpConnectionException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class FTPService
{

    @Value("${ftp.server}")
    private String FTP_SERVER;
    @Value("${ftp.username}")
    private String FTP_USERNAME;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;
    @Value("${ftp.origin}")
    private String FTP_ORIGIN_DIRECTORY;
    @Value("${ftp.port}")
    private int FTP_PORT;

    private FTPClient getFTPConnection() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(FTP_SERVER,FTP_PORT);
        ftpClient.login(FTP_USERNAME,FTP_PASSWORD);

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        return ftpClient;
    }

    private void createFolder(FTPClient ftpClient) throws IOException {
        ftpClient.makeDirectory(FTP_ORIGIN_DIRECTORY+"/"+LocalDate.now());
    }

    private boolean streamFile(MultipartFile file, FTPClient ftpClient, String remoteFilePath) throws IOException {
        InputStream inputStream = file.getInputStream();
        boolean uploaded = ftpClient.storeFile(remoteFilePath,inputStream);
        inputStream.close();
        return uploaded;
    }


    public FileEntity uploadFileToFTP(MultipartFile file) throws FtpConnectionException, IOException {
        try {
            FTPClient ftpClient = getFTPConnection();
            String remoteFilePath = FTP_ORIGIN_DIRECTORY + "/" + LocalDate.now() + "/" + file.getOriginalFilename();
            boolean uploaded = streamFile(file, ftpClient, remoteFilePath);
            if(!uploaded) {
                createFolder(ftpClient);
                if(!streamFile(file, ftpClient, remoteFilePath)) {
                    throw new FtpConnectionException("Can't connect to server");
                }
            }
            ftpClient.logout();
            ftpClient.disconnect();
            return FileEntity.builder().path(remoteFilePath).uuid(UUID.randomUUID().toString()).createAt(LocalDate.now()).isUsed(true).build();
        } catch (IOException e) {
            throw new FtpConnectionException(e);
        }
    }

    public void deleteFile(String path) throws IOException {
        FTPClient ftpClient = getFTPConnection();
        ftpClient.deleteFile(path);
        ftpClient.logout();
        ftpClient.disconnect();
    }

    public ByteArrayOutputStream getFile(FileEntity fileEntity) throws IOException {
        FTPClient ftpClient = getFTPConnection();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean downloaded = ftpClient.retrieveFile(fileEntity.getPath(),outputStream);
        ftpClient.logout();
        ftpClient.disconnect();
        if(downloaded) {
            return outputStream;
        }
        throw new FtpConnectionException("Cannot download file");
    }
}
