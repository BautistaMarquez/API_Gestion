package com.gestion.erp.exception;

// Extiende de BusinessException porque un conflicto es un error de negocio específico
public class ResourceConflictException extends BusinessException {
    public ResourceConflictException(String message) {
        super(message);
    }
}