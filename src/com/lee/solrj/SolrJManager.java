package com.lee.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * SolrJ管理
 * 删除、修改、添加、查询
 * @author lee leeshuhua@163.com
 * @create 2018-04-27 11:04
 **/

public class SolrJManager {

    // 添加
    @Test
    public void testAdd() throws Exception{

        //String baseURL = "http://localhost:8080/solr/collection2";
        String baseURL = "http://localhost:8080/solr/collection2";
        // 单机版
        SolrServer solrServer = new HttpSolrServer(baseURL);

        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id","123");
        doc.setField("name","李书华");
        // 添加
        solrServer.add(doc);
        // 提交
        solrServer.commit();
    }

    // 删除
    @Test
    public void testDelete() throws Exception{

        //String baseURL = "http://localhost:8080/solr/collection2";
        String baseURL = "http://localhost:8080/solr/collection2";
        // 单机版
        SolrServer solrServer = new HttpSolrServer(baseURL);
        // 删除全部
        solrServer.deleteByQuery("*:*",1000);

        solrServer.commit();
    }

    // 更新
    @Test
    public void testUpdate() throws Exception{
        //String baseURL = "http://localhost:8080/solr/collection2";
        String baseURL = "http://localhost:8080/solr/collection2";
        // 单机版
        SolrServer solrServer = new HttpSolrServer(baseURL);
        // 更新
        // 与添加一致，如果id相同就是更新id不同就是添加
    }

    // 查询
    @Test
    public void testSerach() throws Exception{
        //String baseURL = "http://localhost:8080/solr/collection2";
        String baseURL = "http://localhost:8080/solr/collection1";
        // 单机版
        SolrServer solrServer = new HttpSolrServer(baseURL);
        // 查询 关键词 台灯 "product_catalog_name":"幽默杂货",
        //  "product_price": 18.9, 价格排序 分页 开始行 每页数 高亮 默认域 只查询指定域
        SolrQuery solrQuery = new SolrQuery();
        // 关键词
        // solrQuery.set("q","product_name:台灯");
        solrQuery.setQuery("product_name:台灯");
        solrQuery.set("fq","product_catalog_name:幽默杂货");
        solrQuery.set("fq","product_price:[* TO 10]");
        // 价格排序
        solrQuery.addSort("product_price", SolrQuery.ORDER.desc);
        // 分页
        solrQuery.setStart(0);
        solrQuery.setRows(5);
        // 高亮 默认域 指定域
        // 设置默认域
        solrQuery.set("df","product_name");
        // 只查询指定域
        solrQuery.set("fl","id,product_name");
        // 高亮
        // 打开开关
        solrQuery.setHighlight(true);
        // 指定高亮域
        solrQuery.addHighlightField("product_name");
        // 前缀
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        // 后缀
        solrQuery.setHighlightSimplePost("</span>");
        // 执行查询
        QueryResponse response = solrServer.query(solrQuery);
        // 文档结果集
        SolrDocumentList docs = response.getResults();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        // Map K id V Map
        // Map K 域名 V List
        // List List.get(0)
        // 总条数
        long numFound = docs.getNumFound();
        System.out.println(numFound);
        for (SolrDocument doc:docs){
            System.out.println(doc.get("id"));
            System.out.println(doc.get("product_catalog_name"));
            System.out.println(doc.get("product_price"));
            System.out.println(doc.get("product_name"));
            System.out.println(doc.get("product_picture"));

            Map<String, List<String>> map = highlighting.get(doc.get("id"));
            List<String> list = map.get("product_name");
            System.out.println(list.get(0));
        }
    }
}
