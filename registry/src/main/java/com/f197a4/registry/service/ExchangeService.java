package com.f197a4.registry.service;

import java.io.StringReader;
import java.text.NumberFormat;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import mnb.MNBArfolyamServiceSoap;
import mnb.MNBArfolyamServiceSoapImpl;

@Service
public class ExchangeService {
	private static Logger logger = LoggerFactory.getLogger(ExchangeService.class);

	private static MNBArfolyamServiceSoap bankService = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();

    public static Double exchange(Integer what, String to) {
		logger.info("Converting {}HUF to {}.",what,to);
        String base = "";
        try {
            base = bankService.getCurrentExchangeRates();
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(base));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("Rate");
			Double rate;
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				String name = element.getAttribute("curr");
				if(name.equals(to.toUpperCase())) {
					NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
					rate = format.parse(element.getTextContent()).doubleValue();
					Double exchanged = what/rate;
					logger.info("Exchange rate between HUF and {}: {} -> {}HUF is {}{}", to, rate,what,exchanged,to.toUpperCase());
					return exchanged;
				}
			}
			logger.error("No {} in the currency list.", to.toUpperCase());
			return null;

		} catch (Exception e) {
			logger.error("Something went wrong during conversion: {}", e.getMessage());
			return null;	
        }
    }
}
