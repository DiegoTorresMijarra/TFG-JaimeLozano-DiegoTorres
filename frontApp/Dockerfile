# Usar una imagen de base de Node.js para la construcción
FROM node:14 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Usar una imagen de base de Nginx para servir la aplicación
FROM nginx:alpine
COPY --from=build /app/www /usr/share/nginx/html
EXPOSE 4200
