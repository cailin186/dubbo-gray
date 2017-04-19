package com.xlz.commons.base.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件构造器，构建形式如下    .
 *
 List<FilterRule> rules = FilterRuleBuilder.newBuilder()
 .key("name").eq().value("324")
 .and()
 .key("password").like().value("fsd")
 .and()
 .key("add").in().value(new String[]{"111","222"})
 .and()
 .key("address").ge().value("234")
 .build();

 * @author zhangll
 */
public class FilterRuleBuilder {
    private FilterRule filterRule;
    private List<FilterRule> filterRules;

    public static FilterRuleBuilder newBuilder(){
        FilterRuleBuilder builder = new FilterRuleBuilder();
        builder.filterRule = new FilterRule();
        builder.filterRules = new ArrayList<>();
        return builder;
    }
    public FilterRuleBuilder key(String key){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setKey(key);
        return this;
    }
    public FilterRuleBuilder comparator(String comparator){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator(comparator);
        return this;
    }
    public FilterRuleBuilder eq(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("=");
        return this;
    }
    public FilterRuleBuilder like(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("like");
        return this;
    }
    public FilterRuleBuilder ne(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("!=");
        return this;
    }
    public FilterRuleBuilder ge(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator(">=");
        return this;
    }
    public FilterRuleBuilder gt(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator(">");
        return this;
    }
    public FilterRuleBuilder le(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("<=");
        return this;
    }
    public FilterRuleBuilder lt(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("<");
        return this;
    }
    public FilterRuleBuilder between(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("between");
        return this;
    }
    public FilterRuleBuilder isNull(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("isnull");
        return this;
    }
    public FilterRuleBuilder notNull(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("notnull");
        return this;
    }

    public FilterRuleBuilder in(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("in");
        return this;
    }

    public FilterRuleBuilder notIn(){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setComparator("notin");
        return this;
    }

    public FilterRuleBuilder value(Object value){
        if(filterRule == null){
            filterRule = new FilterRule();
        }
        filterRule.setValue(value);
        return this;
    }
    public FilterRuleBuilder and(){
        if(filterRule != null){
            filterRules.add(filterRule);
            filterRule = null;
        }
        return this;
    }
    public List<FilterRule> build(){
        if(filterRule != null){
            filterRules.add(filterRule);
        }
        return filterRules;
    }

    public static void main(String[] args) {
        List<FilterRule> rules = FilterRuleBuilder.newBuilder()
                .key("name").eq().value("324")
                .and()
                .key("password").like().value("fsd")
                .and()
                .key("address").ge().value("234")
                .build();
        for(FilterRule rule : rules){
            System.out.println(rule);
        }
    }
}
