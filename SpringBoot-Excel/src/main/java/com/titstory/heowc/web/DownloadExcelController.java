package com.titstory.heowc.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.titstory.heowc.constant.ExcelConstant;
import com.titstory.heowc.domain.KeyClassSub;
import com.titstory.heowc.domain.KeyClass;

@Controller
@RequestMapping("download")
public class DownloadExcelController {
	
	
	@GetMapping("excel-xls")
	public ModelAndView xlsView() {
		return new ModelAndView("excelXlsView", getDefaultMap());
	}
	
	
	
	@RequestMapping(path = "/excel-xls1/{startDate}", method = RequestMethod.GET)
	public ModelAndView xlsView1(@PathVariable(value = "startDate") String startDate) {
		System.out.println(startDate);
		return new ModelAndView("excelXlsView", getCusomerMap());
	}

	@GetMapping("excel-xlsx")
	public ModelAndView xlsxView() {
		return new ModelAndView("excelXlsxView", getDefaultMap());
	}

	@GetMapping("excel-xlsx-streaming")
	public ModelAndView xlsxStreamingView() {
		return new ModelAndView("excelXlsxStreamingView", getDefaultMap());
	}

	private Map<String, Object> getDefaultMap() {
		Map<String, Object> map = new HashMap<>();
		map.put(ExcelConstant.FILE_NAME, "default_excel");
		map.put(ExcelConstant.HEAD, Arrays.asList("ID", "NAME", "COMMENT"));
		map.put(ExcelConstant.BODY,
				Arrays.asList(
						Arrays.asList("A", "a", "가"),
						Arrays.asList("B", "b", "나"),
						Arrays.asList("C", "c", "다")
				));
		return map;
	}
	
	private Map<String, Object> getCusomerMap() {
		Map<String, Object> map = new HashMap<>();
		map.put(ExcelConstant.FILE_NAME, "default_excel");
		map.put(ExcelConstant.HEAD, Arrays.asList("Rm", "Primay BTT", "Partner BTT", "Visiting Veteran", "Count"));
		map.put(ExcelConstant.BODY,getKeyClass());
		map.put(ExcelConstant.SUB_HEAD, Arrays.asList("EID", "FA#", "P/J#", "Rm", "First Name","Last Name","ST","Area","Region","Status","Kyc BTT"));
		map.put(ExcelConstant.SUB_BODY,getKeyClassSub());
		return map;
	}
	
	private List<KeyClass> getKeyClass(){
		List<KeyClass> sub = new ArrayList<KeyClass>();
		for(int i=1;i<10;i++) {
			KeyClass keySub = new KeyClass();
			keySub.setCount(""+i);
			keySub.setRm("STLRoom-0"+i);
			keySub.setPartnerBtt("BTTPartner-0"+i);
			keySub.setPrimaryBtt("BTTPrimary-0"+i);
			keySub.setVisitingVeteran("VistingVet-0"+i);
			sub.add(keySub);
		}
		return sub;
	}
	
	private List<KeyClassSub> getKeyClassSub(){
		List<KeyClassSub> sub = new ArrayList<KeyClassSub>();
		for(int i=1;i<10;i++) {
			KeyClassSub keySub = new KeyClassSub();
			keySub.setEid("Eid"+i);
			keySub.setFa("FA"+i);
			keySub.setArea("Area"+i);
			keySub.setFirstName("FirstName"+i);
			keySub.setLastName("LastName"+i);
			keySub.setPj("PJ"+i);
			keySub.setRegion("Region"+i);
			keySub.setRm("Rm"+i);
			keySub.setSt("ST"+i);
			keySub.setStatus("Status"+i);
			sub.add(keySub);
		}
		return sub;
	}
}
