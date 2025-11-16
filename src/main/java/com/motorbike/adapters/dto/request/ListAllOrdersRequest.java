package com.motorbike.adapters.dto.request;

/**
 * Request DTO for list all orders endpoint
 */
public class ListAllOrdersRequest {
    private int page;
    private int pageSize;
    private String status;
    private String sortBy;

    public ListAllOrdersRequest() {
        this.page = 0;
        this.pageSize = 10;
        this.sortBy = "date_desc";
    }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
}