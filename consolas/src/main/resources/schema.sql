CREATE TABLE IF NOT EXISTS consolas (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    marca VARCHAR(80) NOT NULL,
    modelo VARCHAR(80) NOT NULL
);

-- Una base de la versión anterior puede conservar los precios ya guardados.
DO 'BEGIN
    ALTER TABLE consolas ALTER COLUMN precio DROP NOT NULL;
EXCEPTION WHEN undefined_column THEN
    NULL;
END';

-- ALTER TABLE consolas ADD COLUMN IF NOT EXISTS precio NUMERIC(12, 2) CHECK (precio >= 0); -- PRECIO OPCIONAL
