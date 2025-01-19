export interface BorrowRecord {
  id: number;
  user: {
    id: number;
    name: string;
  };
  book: {
    id: number;
    title: string;
  };
  borrowDate: string; // ISO string format
  returnDate?: string; // Optional, ISO string format
}