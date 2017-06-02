<?xml version="1.0" encoding="UTF-8"?>
<sitemapindex>
    <sitemap>
        <#list 1..(pagedExperiences.total / pagedExperiences.size)?ceiling as i>
            <loc>www.askdog.com/sitemap${i}.xml</loc>
            <lastmod>${.now?string["yyyy-MM-dd"]}</lastmod>
        </#list>
    </sitemap>
</sitemapindex>