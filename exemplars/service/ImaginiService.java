package com.example.exemplars_service.service;

import com.example.exemplars_service.domain.IImagineDAO;
import com.example.exemplars_service.domain.Imagine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImaginiService {

    private final IImagineDAO imagineDAO;

    public ImaginiService(IImagineDAO imagineDAO) {
        this.imagineDAO = imagineDAO;
    }

    public List<Imagine> getImagini() {
        return imagineDAO.imagini();
    }

    public List<Imagine> getImaginiByExemplarId(int exemplarId) {
        return imagineDAO.findByExemplarId(exemplarId);
    }

    public Imagine getImagine(int id) {
        return imagineDAO.imagineById(id);
    }

    public boolean insertImagine(Imagine imagine) {
        return imagineDAO.insert(imagine);
    }

    public boolean updateImagine(Imagine imagine) {
        return imagineDAO.update(imagine);
    }

    public boolean deleteImagine(int id) {
        return imagineDAO.delete(id);
    }

    public boolean deleteImaginiByExemplarId(int exemplarId) {
        return imagineDAO.deleteByExemplarId(exemplarId);
    }
}