package ra.edu.utils;

import java.util.List;

public class PageInfo<T> {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private int totalRecords;
    private List<T> records;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public List<T> getRecords() {
        return records;
    }
}
