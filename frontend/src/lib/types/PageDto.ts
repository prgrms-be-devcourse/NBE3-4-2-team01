export interface PageDto<T> {
    currentPageNumber : number;
    pageSize : number;
    totalPages : number;
    totalItmes : number;
    items : T[];
}