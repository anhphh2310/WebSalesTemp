package tma.datraining.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tma.datraining.model.Location;
import tma.datraining.model.Product;
import tma.datraining.model.Sales;
import tma.datraining.model.Time;
import tma.datraining.service.LocationService;
import tma.datraining.service.ProductService;
import tma.datraining.service.SalesService;
import tma.datraining.service.TimeService;

@RestController
public class MainController {

	@Autowired
	private LocationService locaSer;

	@Autowired
	private ProductService productSer;

	@Autowired
	private SalesService salesSer;

	@Autowired
	private TimeService timeSer;

	private boolean flag = false;

	@RequestMapping("/")
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
		java.sql.Timestamp time1 = java.sql.Timestamp.valueOf("2018-08-27 10:10:10.0");
		java.sql.Timestamp time2 = java.sql.Timestamp.valueOf("2018-08-28 10:10:10.0");
		UUID locaId = locaSer.save(new Location("VIETNAM", "Ho Chi Minh city", time1, time2));
		UUID locaId2 = locaSer.save(new Location("VIETNAM", "Da Nang city", time1, time2));
		UUID locaId3 = locaSer.save(new Location("VIETNAM", "Ha Noi city", time1, time2));
		UUID locaId4 = locaSer.save(new Location("VIETNAM", "Dong Thap city", time1, time2));

		// Product
		java.sql.Timestamp time3 = java.sql.Timestamp.valueOf("2018-09-27 13:10:10.0");
		java.sql.Timestamp time4 = java.sql.Timestamp.valueOf("2018-09-28 16:10:10.0");
		UUID productId = productSer.save(new Product(100, "SCREEN", "Inv-1", time3, time4));
		UUID productId1 = productSer.save(new Product(105, "CABLE", "Inv-2", time3, time4));
		UUID productId2 = productSer.save(new Product(170, "MOUSE", "Inv-3", time3, time4));
		UUID productId3 = productSer.save(new Product(190, "KEYBOARD", "Inv-4", time3, time4));
		UUID productId4 = productSer.save(new Product(250, "SPEAKER", "Inv-5", time3, time4));
		UUID productId5 = productSer.save(new Product(199, "RECORDER", "Inv-6", time3, time4));
		UUID productId6 = productSer.save(new Product(180, "CORE", "Inv-7", time3, time4));
		UUID productId7 = productSer.save(new Product(201, "PRINTER", "Inv-8", time3, time4));
		UUID productId8 = productSer.save(new Product(120, "SCANNER", "Inv-9", time3, time4));
		UUID productId9 = productSer.save(new Product(231, "STORAGE", "Inv-10", time3, time4));

		// Time
		java.sql.Timestamp time5 = java.sql.Timestamp.valueOf("2018-09-27 1:10:10.0");
		java.sql.Timestamp time6 = java.sql.Timestamp.valueOf("2018-09-28 5:10:10.0");
		UUID timeId = timeSer.save(new Time(9, 27, 2018, time5, time6));
		UUID timeId1 = timeSer.save(new Time(9, 26, 2018, time5, time6));
		UUID timeId2 = timeSer.save(new Time(9, 25, 2018, time5, time6));
		UUID timeId3 = timeSer.save(new Time(9, 24, 2018, time5, time6));

		// Sale
		java.sql.Timestamp time7 = java.sql.Timestamp.valueOf("2018-09-27 3:10:10.0");
		java.sql.Timestamp time8 = java.sql.Timestamp.valueOf("2018-09-28 5:10:10.0");
		salesSer.save(new Sales(productSer.get(productId), timeSer.get(timeId), locaSer.get(locaId),
				new BigDecimal("100.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId1), timeSer.get(timeId1), locaSer.get(locaId),
				new BigDecimal("100.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId1), timeSer.get(timeId1), locaSer.get(locaId2),
				new BigDecimal("150.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId3), timeSer.get(timeId2), locaSer.get(locaId4),
				new BigDecimal("90.78"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId5), timeSer.get(timeId3), locaSer.get(locaId4),
				new BigDecimal("190.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId9), timeSer.get(timeId2), locaSer.get(locaId3),
				new BigDecimal("462.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId2), timeSer.get(timeId3), locaSer.get(locaId3),
				new BigDecimal("462.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId4), timeSer.get(timeId2), locaSer.get(locaId),
				new BigDecimal("462.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId6), timeSer.get(timeId1), locaSer.get(locaId),
				new BigDecimal("462.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId7), timeSer.get(timeId2), locaSer.get(locaId2),
				new BigDecimal("462.0"), time7, time8));
		salesSer.save(new Sales(productSer.get(productId8), timeSer.get(timeId), locaSer.get(locaId2),
				new BigDecimal("462.0"), time7, time8));
	}
}
