export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: number;
  available: boolean;
  description?: string;
  publisher?: string;
  publicationYear?: number;
  genre?: string;
}
