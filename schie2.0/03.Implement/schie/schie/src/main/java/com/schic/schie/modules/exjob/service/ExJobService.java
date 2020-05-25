/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: </p>
 */
package com.schic.schie.modules.exjob.service;

import java.util.Iterator;
import java.util.List;

import com.jeespring.common.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeespring.common.service.AbstractBaseService;
import com.jeespring.common.utils.StringUtils;
import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.database.service.ExDbService;
import com.schic.schie.modules.exask.entity.ExResAsk;
import com.schic.schie.modules.exjob.dao.ExJobDao;
import com.schic.schie.modules.exjob.entity.ExJob;
import com.schic.schie.modules.resource.entity.ExResources;

/**
 * <p>Title: ExJobService</p>
 * <p>Description: </p>
 *
 * @author caiwb
 * @date 2019年8月15日
 */
@Service
@Transactional(readOnly = true)
public class ExJobService extends AbstractBaseService<ExJobDao, ExJob> {

    @Autowired
    private ExDbService exDbService;

    public ExJobService() {
        //
    }

    @Override
    public Page<ExJob> findPage(Page<ExJob> page, ExJob entity) {
        entity.setPage(page);
        page.setList(findAllList(entity));
        return page;
    }

    @Override
    public List<ExJob> findAllList(ExJob exJobParam) {
        List<ExJob> list = super.findAllList(exJobParam);
        Iterator<ExJob> iterator = list.iterator();
        while (iterator.hasNext()) {
            ExJob exJob = iterator.next();
            //解析json字段
            ExResources exResources = exJob.getRes();

            exResources.parseResJson();

            if (StringUtils.isEmpty(exResources.getDbid())) {
                logger.error("资源的数据库未配置{}，放弃任务{}", exResources.getResJson(), exResources.getName());
                iterator.remove();
                continue;
            }

            ExDb resDb = exDbService.get(exResources.getDbid());
            if (resDb == null) {
                logger.error("资源的数据库id无效{}，放弃任务{}", exResources.getDbid(), exResources.getName());
                iterator.remove();
                continue;
            }
            exJob.setResDb(resDb);

            if (StringUtils.isEmpty(exResources.getSql())) {
                logger.error("资源的执行sql未配置{}，放弃任务{}", exResources.getResJson(), exResources.getName());
                iterator.remove();
                continue;
            }

            exResources.parseSubJson();

            ExResAsk exResAsk = exJob.getResAsk();
            exResAsk.parseDbSub();
            if (exResAsk.getExResAskDbSub() == null) {
                logger.error("资源请求数据库配置错误{}，放弃任务{}", exResAsk.getSubJson(), exResources.getName());
                iterator.remove();
                continue;
            }

            if (StringUtils.isEmpty(exResAsk.getExResAskDbSub().getDbId())) {
                logger.error("资源请求数据库未配置，放弃任务{}", exResources.getName());
                iterator.remove();
                continue;
            }

            ExDb askSubDb = exDbService.get(exResAsk.getExResAskDbSub().getDbId());
            if (askSubDb == null) {
                logger.error("资源请求的数据库id无效{}，放弃任务{}", exResAsk.getExResAskDbSub().getDbId(), exResources.getName());
                iterator.remove();
                continue;
            }
            exJob.setResAskDb(askSubDb);

            exResAsk.parseInOutMap();

            if (exResAsk.getListExResAskInOutMap() == null) {
                logger.error("资源请求字段映射配置错误，放弃任务{}", exResources.getName());
                iterator.remove();
                continue;
            }
            exJob.setId(exResources.getId() + "." + exResAsk.getId());

            if (StringUtils.isEmpty(exJob.getResAsk().getDbresSubNow())) {
                exJob.getResAsk().setDbresSubNow(exJob.getResAsk().getExResAskDbSub().getIncInitValue());
            }
        }
        return list;
    }

}
