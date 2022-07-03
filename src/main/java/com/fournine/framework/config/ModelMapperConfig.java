package com.fournine.framework.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

//	@Bean
//	public ModelMapper modelMapper() {
//		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.getConfiguration().setFieldMatchingEnabled(true)
//				.setPropertyCondition(new Condition<Object, Object>() {
//					public boolean applies(MappingContext<Object, Object> context) {
//						return !(context.getSource() instanceof PersistentCollection);
//					}
//				}).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
//				.setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR)
////				.setMatchingStrategy(MatchingStrategies.STRICT);
//				.setAmbiguityIgnored(true);
//
//		return modelMapper;
//		// https://github.com/modelmapper/modelmapper/issues/212
//	}
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}
}
