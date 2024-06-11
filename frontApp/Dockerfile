# Etapa de construcción
FROM node:20 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npx ionic build --prod

# Etapa de producción
FROM nginx:alpine
COPY --from=build /app/www /usr/share/nginx/html
EXPOSE 80
