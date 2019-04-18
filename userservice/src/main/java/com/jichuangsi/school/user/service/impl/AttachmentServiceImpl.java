package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.school.user.entity.backstage.SchoolAttachment;
import com.jichuangsi.school.user.model.file.UserFile;
import com.jichuangsi.school.user.repository.backstage.ISchoolAttachmentRepository;
import com.jichuangsi.school.user.service.IAttachmentService;
import com.jichuangsi.school.user.service.IFileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

@Service
public class AttachmentServiceImpl implements IAttachmentService {

    @Autowired
    private ISchoolAttachmentRepository schoolAttachmentRepository;
    @Autowired
    private IFileStoreService fileStoreService;

    @Override
    public void saveAttachments() {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        Resource resource = new ClassPathResource("static/xml");
        try {
            File file = resource.getFile();
            if (file.exists() || !file.isFile()){
                File[] files = file.listFiles();
                for (File newFile : files){
                    if (schoolAttachmentRepository.countByAndOriginalName(newFile.getName()) > 0){
                        continue;
                    }
                    String contentType = Files.probeContentType(newFile.toPath());
                    in =  new FileInputStream(newFile);
                    out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int n = 0;
                    while ((n = in.read(buffer)) != -1) {
                        out.write(buffer, 0, n);
                    }
                    UserFile userFile = new UserFile(contentType,newFile.getName(),buffer);
                    fileStoreService.uploadFile(userFile);
                    SchoolAttachment attachment = new SchoolAttachment();
                    attachment.setContentType(contentType);
                    attachment.setOriginalName(newFile.getName());
                    attachment.setSubName(userFile.getSubName());
                    schoolAttachmentRepository.save(attachment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
