package org.schic.modules.utils.service;

import org.schic.common.utils.SendMailUtil;
import org.schic.modules.sys.entity.SysConfig;
import org.schic.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 
 * @Description: 发邮件服务
 * @author Caiwb
 * @date 2019年5月6日 下午2:51:49
 */
@Service
public class EmailService {
	@Autowired
	private SysConfigService sysConfigService;

	public void sendMailException(
			@RequestParam(required = false) String subject,
			@RequestParam(required = false) String message) {
		SysConfig entity = new SysConfig();
		entity.setType("toExceptionMailAddr");
		List<SysConfig> sysConfigList = sysConfigService.findList(entity);
		entity.setType("ExceptionMailRun");
		entity = sysConfigService.findListFirstCache(entity);
		if (!sysConfigList.isEmpty() && "true".equals(entity.getValue())) {
			SendMailUtil.sendCommonMail(sysConfigList.get(0).getValue(),
					"(异常邮件)" + subject, message);
		}
	}
}
