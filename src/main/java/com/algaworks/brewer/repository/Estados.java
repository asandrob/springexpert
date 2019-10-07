package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Estado;
import com.algaworks.brewer.repository.helper.estado.EstadosQueries;

@Repository
public interface Estados extends JpaRepository<Estado, Long>, EstadosQueries {

}
