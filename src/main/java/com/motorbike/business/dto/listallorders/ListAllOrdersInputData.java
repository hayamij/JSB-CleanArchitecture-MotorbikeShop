package com.motorbike.business.dto.listallorders;

public class ListAllOrdersInputData {
    private final int page;
    private final int pageSize;
    private final String filterStatus;
    private final String sortBy;

    private ListAllOrdersInputData(int page, int pageSize, String filterStatus, String sortBy) {
        this.page = page;
        this.pageSize = pageSize;
        this.filterStatus = filterStatus;
        this.sortBy = sortBy;
    }

    public static ListAllOrdersInputData getAllOrders() {return new ListAllOrdersInputData(0, 10, null, "date_desc");}

    public static ListAllOrdersInputData withPagination(int page, int pageSize) {
        return new ListAllOrdersInputData(page, pageSize, null, "date_desc");
    }

    public static ListAllOrdersInputData withStatusFilter(String status) {
        return new ListAllOrdersInputData(0, 10, status, "date_desc");
    }

    public static ListAllOrdersInputData withFullFilters(int page, int pageSize, String status, String sortBy) {
        return new ListAllOrdersInputData(page, pageSize, status, sortBy);
    }

    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }
    public String getFilterStatus() { return filterStatus; }
    public String getSortBy() { return sortBy; }
    public int getOffset() { return page * pageSize; }
    public boolean hasStatusFilter() { return filterStatus != null && !filterStatus.trim().isEmpty(); }
}
