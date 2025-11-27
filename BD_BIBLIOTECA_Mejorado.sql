/*
-------------------------------------------------
 SCRIPT COMPLETO Y CORREGIDO: BD_BIBLIOTECA
-------------------------------------------------
*/
use master
 
CREATE DATABASE BD_BIBLIOTECA;
GO

USE BD_BIBLIOTECA;
GO

-- --- TABLA DE USUARIOS ---
CREATE TABLE Usuario(
	IdUsuario INT IDENTITY(1,1) PRIMARY KEY,
	Nombres VARCHAR(100) NOT NULL,
	Apellidos VARCHAR(100) NOT NULL,
	Email VARCHAR(200) UNIQUE NOT NULL,
	PasswordHash NVARCHAR(100) NOT NULL,
	Telefono INT,
	Direccion VARCHAR(300),
	FechaCreacion DATETIME DEFAULT GETDATE(),
	Estado BIT DEFAULT 1
);
GO

-- --- TABLA DE AUTORES ---
CREATE TABLE Autores(
	IdAutor INT IDENTITY(1,1) PRIMARY KEY,
	Nombres VARCHAR(100) NOT NULL,
	Apellidos VARCHAR(100) NOT NULL,
	FechaNacimiento DATE,
	Nacionalidad VARCHAR(120),
	FechaCreacion DATETIME DEFAULT GETDATE(),
	Estado BIT DEFAULT 1
);
GO

-- --- TABLA DE CATEGORÍAS ---
CREATE TABLE Categorias(
	IdCategoria INT IDENTITY(1,1) PRIMARY KEY,
	Nombre VARCHAR(100) NOT NULL UNIQUE,
	Descripcion NVARCHAR(MAX),
	Estado BIT DEFAULT 1
);
GO

-- --- TABLA DE LIBROS (Títulos) ---
CREATE TABLE Libros (
    IdLibro INT IDENTITY(1,1) PRIMARY KEY,
    Titulo VARCHAR(200) NOT NULL,
    ISBN VARCHAR(50) UNIQUE,
    IdCategoria INT FOREIGN KEY REFERENCES Categorias(IdCategoria),
    FechaPublicacion DATE,
    Idioma VARCHAR(50),
    NumeroPaginas INT,
    Descripcion NVARCHAR(MAX),
    FechaCreacion DATETIME DEFAULT GETDATE(),
    Estado BIT DEFAULT 1
);
GO

-- --- TABLA INTERMEDIA LIBRO-AUTOR ---
CREATE TABLE LibroAutor (
    IdLibro INT FOREIGN KEY REFERENCES Libros(IdLibro),
    IdAutor INT FOREIGN KEY REFERENCES Autores(IdAutor),
    PRIMARY KEY (IdLibro, IdAutor)
);
GO

-- --- (NUEVA) TABLA DE EJEMPLARES ---
CREATE TABLE Ejemplares (
    IdEjemplar INT IDENTITY(1,1) PRIMARY KEY,
    IdLibro INT NOT NULL FOREIGN KEY REFERENCES Libros(IdLibro),
    CodigoEjemplar VARCHAR(50) UNIQUE NOT NULL,
    EstadoCopia VARCHAR(20) NOT NULL 
        CHECK (EstadoCopia IN ('Disponible', 'EnReparacion', 'Perdido', 'Prestado')) DEFAULT 'Disponible',
    FechaAdquisicion DATE,
    FechaCreacion DATETIME DEFAULT GETDATE(),
    Estado BIT DEFAULT 1
);
GO

-- --- TABLA DE PRÉSTAMOS ---
CREATE TABLE Prestamos (
    IdPrestamo INT IDENTITY(1,1) PRIMARY KEY,
    IdEjemplar INT NOT NULL,
    IdUsuario INT NOT NULL,
    FechaPrestamo DATETIME DEFAULT GETDATE(),
    FechaDevolucionEsperada DATETIME NOT NULL,
    FechaDevolucionReal DATETIME,
    EstadoPrestamo VARCHAR(20)
        CHECK (EstadoPrestamo IN ('Activo', 'Devuelto', 'Vencido')) DEFAULT 'Activo',
    FechaCreacion DATETIME DEFAULT GETDATE(),
    Estado BIT DEFAULT 1,
    
    -- Modificación de las Claves Foráneas
    CONSTRAINT FK_Prestamos_Ejemplares FOREIGN KEY (IdEjemplar) REFERENCES Ejemplares(IdEjemplar),
    CONSTRAINT FK_Prestamos_Usuario FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario)
);
GO

-- --- CREACIÓN DE ÍNDICES ---
CREATE NONCLUSTERED INDEX IX_Libros_IdCategoria ON Libros(IdCategoria);
CREATE NONCLUSTERED INDEX IX_LibroAutor_IdAutor ON LibroAutor(IdAutor);
CREATE NONCLUSTERED INDEX IX_Ejemplares_IdLibro ON Ejemplares(IdLibro);
CREATE NONCLUSTERED INDEX IX_Prestamos_IdEjemplar ON Prestamos(IdEjemplar);
CREATE NONCLUSTERED INDEX IX_Prestamos_IdUsuario ON Prestamos(IdUsuario);

-- También es útil indexar campos de búsqueda común
CREATE NONCLUSTERED INDEX IX_Libros_Titulo ON Libros(Titulo);
CREATE NONCLUSTERED INDEX IX_Autores_Apellidos ON Autores(Apellidos);
GO

PRINT 'Base de datos BD_BIBLIOTECA creada y estructurada exitosamente.';
GO