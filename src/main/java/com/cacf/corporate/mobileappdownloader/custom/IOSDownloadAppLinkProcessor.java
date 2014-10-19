package com.cacf.corporate.mobileappdownloader.custom;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractSingleAttributeModifierAttrProcessor;
import org.thymeleaf.spring4.requestdata.RequestDataValueProcessorUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

/**
 * Created by cacf on 10/10/14.
 */
public class IOSDownloadAppLinkProcessor extends AbstractSingleAttributeModifierAttrProcessor {

    public static final int ATTR_PRECEDENCE = 1000;
    public static final String ATTR_NAME = "ios_install_link";

    public static final String IOS_OTA_DL_LINK_PREFIX = "itms-services://?action=download-manifest&url=";

    public IOSDownloadAppLinkProcessor() {
        super(ATTR_NAME);
    }


    @Override
    protected String getTargetAttributeName(Arguments arguments, Element element, String attributeName) {
        return "href";
    }

    @Override
    protected String getTargetAttributeValue(Arguments arguments, Element element, String attributeName) {

        String attrValue = getTargetAttrVal(arguments, element, attributeName);
        // return RequestDataValueProcessorUtils.processUrl(arguments.getConfiguration(), arguments, attrValue);
        return IOS_OTA_DL_LINK_PREFIX + RequestDataValueProcessorUtils.processUrl(arguments.getConfiguration(), arguments, attrValue);
    }

    @Override
    protected ModificationType getModificationType(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }

    @Override
    protected boolean removeAttributeIfEmpty(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return true;
    }

    @Override
    protected boolean recomputeProcessorsAfterExecution(Arguments arguments, Element element, String attributeName) {
        return false;
    }

    @Override
    public int getPrecedence() {
        return ATTR_PRECEDENCE;
    }


    private String getTargetAttrVal(
            final Arguments arguments, final Element element, final String attributeName) {

        final String attributeValue = element.getAttributeValue(attributeName);

        final Configuration configuration = arguments.getConfiguration();
        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);

        final IStandardExpression expression = expressionParser.parseExpression(configuration, arguments, attributeValue);

        final Object result = expression.execute(configuration, arguments);
        return (result == null ? "" : result.toString());

    }
}
