package com.larry.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.parameter.SearchParameter;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.larry.model.App;
import com.larry.model.AppSpec;
import com.larry.model.Staff;
import com.larry.repository.AppRepository;
import com.larry.repository.StaffRepository;

@Controller
public class AppController {

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private StaffRepository staffRepository;

	/**
	 * 查询
	 *
	 * @param input
	 *            DataTablesInput
	 * @return DataTablesOutput<App>
	 */
	@ResponseBody
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public DataTablesOutput<App> messages(@Valid DataTablesInput input) {
		try {
			return appRepository.findAll(input, AppSpec.isNotDelete());
		} catch (Exception e) {
			return null;
		}
		// return appRepository.findAll(input);
	}

	@ResponseBody
	@RequestMapping(value = "all2", method = RequestMethod.GET)
	public DataTablesOutput<Staff> messages2(@Valid DataTablesInput input) {
		try {
			SearchParameter searchParameter = input.getSearch();
			if ("".equals(searchParameter.getValue()))
				return staffRepository.findAll(input);

			return staffRepository.findAll(input);
		} catch (Exception e) {
			return null;
		}
		// return appRepository.findAll(input);
	}

	/**
	 * 初始化数据
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "init", method = RequestMethod.GET)
	public String initData() {

		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < 12; i++) {
			App app = new App();
			app.setUrl("http://suqun.github.io/archives/");
			app.setKeywords("key" + i);
			app.setDescription("spring data" + i);
			app.setName("app" + i);
			app.setHot(i);
			app.setDisabled(true);
			// app.setCreateTime(new Date());
			// app.setUpdateTime(new Date());
			appRepository.save(app);
		}

		map.put("msg", "success");
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "addData", method = RequestMethod.POST)
	public String addData(@ModelAttribute Staff staff) {
		staffRepository.save(staff);

		Map<String, String> map = new HashMap<>();
		map.put("msg", "success");
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "editData", method = RequestMethod.POST)
	public String editData(@ModelAttribute Staff staff) {
		staffRepository.save(staff);

		Map<String, String> map = new HashMap<>();
		map.put("msg", "success");
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping(value = "del", method = RequestMethod.GET)
	public String delData(@ModelAttribute Staff staff) {
		staffRepository.delete(staff);

		Map<String, String> map = new HashMap<>();
		map.put("msg", "success");
		return JSON.toJSONString(map);
	}
}
