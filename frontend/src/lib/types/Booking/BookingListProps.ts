export const enum View {
    All,
    User,
    Hotel
};

export type BookingListProps = {
    view : View;
    page? : number;
    pageSize? : number;
};