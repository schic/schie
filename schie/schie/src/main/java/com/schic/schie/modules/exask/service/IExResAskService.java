/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exask.service;

import com.jeespring.common.persistence.InterfaceBaseService;
import com.schic.schie.modules.exask.entity.ExResAsk;

/**
 * I资源申请表Service
 * @author leodeyang
 * @version 2019-08-12
 */
public interface IExResAskService extends InterfaceBaseService<ExResAsk> {

    int updateSubNow(ExResAsk resAsk);
}
