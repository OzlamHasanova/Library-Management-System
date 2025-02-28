package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @Mock
    private Uploader uploader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidFile_whenUploadFile_thenReturnSecureUrl() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image content".getBytes());

        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/some-url");

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);

        String result = cloudinaryService.uploadFile(file);
        assertEquals("https://cloudinary.com/some-url", result);
    }

    @Test
    void givenIOException_whenUploadFile_thenThrowRuntimeException() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image content".getBytes());
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenThrow(new IOException("Upload failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cloudinaryService.uploadFile(file);
        });
        assertEquals("Cloudinary upload error: Upload failed", exception.getMessage());
    }
}
