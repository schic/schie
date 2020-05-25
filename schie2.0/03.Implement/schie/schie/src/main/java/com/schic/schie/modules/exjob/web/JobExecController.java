/**
 *
 */
package com.schic.schie.modules.exjob.web;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.web.AbstractBaseController;
import com.schic.schie.modules.database.entity.ExDb;
import com.schic.schie.modules.exjob.entity.ExJob;
import com.schic.schie.modules.exjob.service.ExJobService;
import com.schic.schie.modules.exjob.service.IExJobExecService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "${adminPath}/jobexec")
public class JobExecController extends AbstractBaseController {

    @Autowired
    private IExJobExecService jobExecService;

    @Autowired
    private ExJobService exJobService;

    public JobExecController() {
        //
    }

    @RequiresPermissions("jobexec:run")
    @RequestMapping(value = {"run"})
    @ResponseBody
    public String run(HttpServletRequest request, HttpServletResponse response, Model model) {
        String jobId = request.getParameter("jobId");
        return jobExecService.runJob(jobId);
    }

    @RequiresPermissions("jobexec:list")
    @RequestMapping(value = {"list"})
    public String list(ExJob exJob, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExJob> page = exJobService.findPage(new Page<>(request, response), exJob);

        model.addAttribute("page", page);
        return "modules/exjob/exJobList";
    }

}
