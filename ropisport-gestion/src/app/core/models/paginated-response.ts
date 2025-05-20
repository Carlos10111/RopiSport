export interface PaginatedResponse<T> {
    content: T[];           // Lista de elementos de la página actual
    page: number;           // Número de página actual (comienza en 0)
    size: number;           // Tamaño de la página (número de elementos por página)
    totalElements: number;  // Total de elementos en todas las páginas
    totalPages: number;     // Número total de páginas
    last: boolean;          // ¿Es esta la última página?
  }