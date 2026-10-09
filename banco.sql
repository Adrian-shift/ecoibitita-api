-- ============================================================
-- ESQUEMA DO BANCO DE DADOS RELACIONAL (POSTGRESQL / NUVEM)
-- PROJETO: EcoIbititá
-- ============================================================

-- 1. TABELA DE PERFIS / NÍVEIS DE ACESSO
CREATE TABLE perfis (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO perfis (nome) VALUES 
('Administrador'), 
('Fiscal'), 
('Cidadão');

-- 2. TABELA DE USUÁRIOS
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    perfil_id INT NOT NULL REFERENCES perfis(id),
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. TABELA DE CATEGORIAS DE RESÍDUOS
CREATE TABLE categorias_residuos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO categorias_residuos (nome) VALUES 
('Entulho'), 
('Lixo Doméstico'), 
('Móveis'), 
('Pneus'), 
('Podas'), 
('Resíduos Eletrônicos');

-- 4. TABELA PRINCIPAL DE DENÚNCIAS
CREATE TABLE denuncias (
    id SERIAL PRIMARY KEY,
    protocolo VARCHAR(20) NOT NULL UNIQUE,
    usuario_id INT REFERENCES usuarios(id) ON DELETE SET NULL,
    categoria_id INT NOT NULL REFERENCES categorias_residuos(id),
    
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    endereco VARCHAR(255),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) DEFAULT 'Ibititá',
    ponto_referencia VARCHAR(255),
    
    descricao TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'Recebida',
    
    data_registro TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. TABELA DE IMAGENS DAS OCORRÊNCIAS
CREATE TABLE imagens_denuncia (
    id SERIAL PRIMARY KEY,
    denuncia_id INT NOT NULL REFERENCES denuncias(id) ON DELETE CASCADE,
    url_imagem VARCHAR(500) NOT NULL,
    public_id_cloudinary VARCHAR(150),
    tipo_foto VARCHAR(20) DEFAULT 'Ocorrência',
    enviado_por INT REFERENCES usuarios(id) ON DELETE SET NULL,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. TABELA DE OBSERVAÇÕES OPERACIONAIS
CREATE TABLE observacoes_operacionais (
    id SERIAL PRIMARY KEY,
    denuncia_id INT NOT NULL REFERENCES denuncias(id) ON DELETE CASCADE,
    fiscal_id INT NOT NULL REFERENCES usuarios(id),
    observacao TEXT NOT NULL,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. TABELA DE HISTÓRICO DE STATUS
CREATE TABLE historico_status (
    id SERIAL PRIMARY KEY,
    denuncia_id INT NOT NULL REFERENCES denuncias(id) ON DELETE CASCADE,
    usuario_id INT NOT NULL REFERENCES usuarios(id),
    status_anterior VARCHAR(30),
    status_novo VARCHAR(30) NOT NULL,
    observacao VARCHAR(255),
    alterado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 8. TABELA DE NOTIFICAÇÕES
CREATE TABLE notificacoes (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    denuncia_id INT NOT NULL REFERENCES denuncias(id) ON DELETE CASCADE,
    mensagem TEXT NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    enviada_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 9. TABELA DE LOGS E AUDITORIA DO SISTEMA
CREATE TABLE logs_auditoria (
    id SERIAL PRIMARY KEY,
    usuario_id INT REFERENCES usuarios(id) ON DELETE SET NULL,
    acao VARCHAR(100) NOT NULL,
    entidade VARCHAR(50),
    entidade_id INT,
    detalhes TEXT,
    data_hora TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ÍNDICES DE DESEMPENHO
CREATE INDEX idx_denuncias_bairro ON denuncias(bairro);
CREATE INDEX idx_denuncias_status ON denuncias(status);
CREATE INDEX idx_denuncias_categoria ON denuncias(categoria_id);
CREATE INDEX idx_denuncias_protocolo ON denuncias(protocolo);
CREATE INDEX idx_denuncias_data ON denuncias(data_registro);
CREATE INDEX idx_imagens_denuncia_id ON imagens_denuncia(denuncia_id);


select * from configuracoes_sistema;


CREATE TABLE configuracoes_sistema (
    id SERIAL PRIMARY KEY,
    chave VARCHAR(100) NOT NULL UNIQUE,
    valor VARCHAR(255) NOT NULL,
    atualizado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO configuracoes_sistema (chave, valor) VALUES ('DENUNCIA_ANONIMA_HABILITADA', 'true');

CREATE TABLE recuperacao_senha (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    expira_em TIMESTAMP WITH TIME ZONE NOT NULL,
    usado BOOLEAN DEFAULT FALSE
);