package com.codi.laolaiqiao.common.jdbc;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, Serializable>, JpaSpecificationExecutor<T> {

}
