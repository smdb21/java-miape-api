package org.proteored.miapeapi.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

/**
 * The class itself is just a few lines of code that delegates to
 * XmlBeanDefinitionReader
 * 
 * @author Salva
 * 
 */
@SuppressWarnings({ "serial", "all" })
public class XmlBeanFactory extends DefaultListableBeanFactory {
	private static final Logger log = Logger.getLogger(XmlBeanFactory.class);
	private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(
			this);

	public XmlBeanFactory(Resource resource) throws BeansException {

		this(resource, null);
	}

	public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory)
			throws BeansException {

		super(parentBeanFactory);
		log.info("reader is " + reader);

		log.info("Resource is " + resource);
		reader.loadBeanDefinitions(resource);
		log.info("After loadBeanDefinitions");
	}

}