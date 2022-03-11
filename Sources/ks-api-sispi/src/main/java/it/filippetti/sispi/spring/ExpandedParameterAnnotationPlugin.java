package it.filippetti.sispi.spring;

import java.lang.annotation.Annotation;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

@Component
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
public class ExpandedParameterAnnotationPlugin implements ModelPropertyBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		// we simply support all documentationTypes!
		return true;
	}

	@Override
	public void apply(ModelPropertyContext context) {
		final Boolean notBlank = annotationNotBlankPresent(context);
		final Boolean notEmpty = annotationNotEmptyPresent(context);
		if (notBlank || notEmpty) {
			context.getSpecificationBuilder().required(true);
		}
	}

	private Boolean annotationNotBlankPresent(ModelPropertyContext context) {
		return annotationPresent(context, NotBlank.class);
//		Boolean result = Boolean.FALSE;
//		Optional<NotBlank> beanAnnotation = Validators.annotationFromBean(context, NotBlank.class);
//		if (beanAnnotation.isPresent()) {
//			result = Boolean.TRUE;
//		}
//		beanAnnotation = Validators.annotationFromField(context, NotBlank.class);
//		if (beanAnnotation.isPresent()) {
//			result = Boolean.TRUE;
//		}
//		return result;
	}

	private Boolean annotationNotEmptyPresent(ModelPropertyContext context) {
		return annotationPresent(context, NotEmpty.class);
//		Boolean result = Boolean.FALSE;
//		Optional<NotEmpty> beanAnnotation = Validators.annotationFromBean(context, NotEmpty.class);
//		if (beanAnnotation.isPresent()) {
//			result = Boolean.TRUE;
//		}
//		beanAnnotation = Validators.annotationFromField(context, NotEmpty.class);
//		if (beanAnnotation.isPresent()) {
//			result = Boolean.TRUE;
//		}
//		return result;
	}

	private <T extends Annotation> Boolean annotationPresent(ModelPropertyContext context, Class<T> class1) {
		Boolean result = Boolean.FALSE;
		Optional<T> beanAnnotation = Validators.annotationFromBean(context, class1);
		if (beanAnnotation.isPresent()) {
			result = Boolean.TRUE;
		}
		beanAnnotation = Validators.annotationFromField(context, class1);
		if (beanAnnotation.isPresent()) {
			result = Boolean.TRUE;
		}
		return result;
	}

}
