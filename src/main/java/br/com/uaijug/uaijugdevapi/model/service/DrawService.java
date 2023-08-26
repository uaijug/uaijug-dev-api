package br.com.uaijug.uaijugdevapi.model.service;

import br.com.uaijug.uaijugdevapi.model.domain.Associate;

import java.util.Set;

public interface DrawService {

    Set<Associate> prizeDrawing(int total);
}
