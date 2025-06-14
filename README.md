# Api de sensores GrowTech Jaroso

## Development

### Requisitos
- Docker
- Docker Compose
- Java 21 Amazon Corretto como JDK

### Instalación
- Clonar el repositorio
```bash
  git clone https://github.com/growtech-jaroso/api-sensores.git && cd api-sensores
```

- Configurar variables de entorno
```bash
  cp .env.template .env
```

- Editar las variables de entorno del .env
- Ejecutar el proyecto
```bash
  docker compose up -d && bun run dev
```