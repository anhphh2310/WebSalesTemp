package tma.datraining.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.model.cassandra.CassLocation;
import tma.datraining.model.cassandra.CassProduct;
import tma.datraining.model.cassandra.CassSales;
import tma.datraining.model.cassandra.CassTime;
import tma.datraining.service.LocationService;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;

@RestController
public class MainController {

	@Autowired
	private LocationService cassLocaSer;

	@Autowired
	private ProductService cassProductSer;

	@Autowired
	private SalesService cassSalesSer;

	@Autowired
	private TimeService cassTimeSer;

	private boolean flag = false;

	@RequestMapping("/home")
	@ResponseBody
	public String getMainPage() {
		if (flag == false) {
			autoCreate();
			flag = true;
		}
		return "Welcome to WebSales";
	}

	public void autoCreate() {
		// Location
		UUID idL1 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "VIETNAM", "Ho Chi Minh City"));
		UUID idL2 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "VIETNAM", "Ha Noi City"));
		UUID idL3 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "CHINA", "Beijing"));
		UUID idL4 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "KOREA", "Seoul City"));
		UUID idL5 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "JAPAN", "Tokyo City"));
		UUID idL6 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "RUSSIA", "Moscow City"));
		UUID idL7 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "ENGLAND", "London City"));
		UUID idL8 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "AMERICAN", "Ho Chi Minh City"));
		UUID idL9 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "CANADA", "Ottawa City"));
		UUID idL10 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "AUSTRALIA", "Canbera City"));
		UUID idL11 = cassLocaSer.saveCass(new CassLocation(UUID.randomUUID(), "SPAIN", "Madrid City"));

		// Product

		UUID idP1 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 100, "CREEN", "Inv-1"));
		UUID idP2 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 100, "SCREEN", "Inv-1"));
		UUID idP3 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 105, "CABLE", "Inv-2"));
		UUID idP4 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 170, "MOUSE", "Inv-3"));
		UUID idP5 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 190, "KEYBOARD", "Inv-4"));
		UUID idP6 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 250, "SPEAKER", "Inv-5"));
		UUID idP7 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 199, "RECORDER", "Inv-6"));
		UUID idP8 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 180, "CORE", "Inv-7"));
		UUID idP9 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 201, "PRINTER", "Inv-8"));
		UUID idP10 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 120, "SCANNER", "Inv-9"));
		UUID idP11 = cassProductSer.saveCass(new CassProduct(UUID.randomUUID(), 231, "STORAGE", "Inv-10"));

		// Time

		UUID idT1 = cassTimeSer.saveCass(new CassTime(UUID.randomUUID(), 9, 27, 2018));
		UUID idT2 = cassTimeSer.saveCass(new CassTime(UUID.randomUUID(), 9, 26, 2018));
		UUID idT3 = cassTimeSer.saveCass(new CassTime(UUID.randomUUID(), 9, 25, 2018));
		UUID idT4 = cassTimeSer.saveCass(new CassTime(UUID.randomUUID(), 9, 24, 2018));

		// Sale

		cassSalesSer.saveCass(new CassSales(idP1, idT1, idL1, new BigDecimal("100.0")));
		cassSalesSer.saveCass(new CassSales(idP2, idT2, idL5, new BigDecimal("100.0")));
		cassSalesSer.saveCass(new CassSales(idP3, idT4, idL4, new BigDecimal("150.0")));
		cassSalesSer.saveCass(new CassSales(idP4, idT1, idL6, new BigDecimal("90.78")));
		cassSalesSer.saveCass(new CassSales(idP5, idT3, idL2, new BigDecimal("190.0")));
		cassSalesSer.saveCass(new CassSales(idP7, idT2, idL3, new BigDecimal("462.0")));
		cassSalesSer.saveCass(new CassSales(idP8, idT2, idL7, new BigDecimal("462.0")));
		cassSalesSer.saveCass(new CassSales(idP9, idT4, idL9, new BigDecimal("462.0")));
		cassSalesSer.saveCass(new CassSales(idP10, idT4, idL10, new BigDecimal("462.0")));
		cassSalesSer.saveCass(new CassSales(idP11, idT1, idL11, new BigDecimal("462.0")));
		cassSalesSer.saveCass(new CassSales(idP6, idT3, idL8, new BigDecimal("462.0")));
		

	}

}
