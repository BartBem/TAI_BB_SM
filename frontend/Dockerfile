# Etap 1: Budowanie (Node.js)
FROM node:18-alpine AS build
WORKDIR /app

# Instalacja zależności
COPY package.json package-lock.json ./
RUN npm install

# Kopiowanie kodu i budowanie
COPY . .
RUN npm run build

# Etap 2: Serwowanie (Nginx)
FROM nginx:alpine
# Kopiowanie zbudowanych plików do folderu Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Prosta konfiguracja Nginx (opcjonalnie można dodać własną nginx.conf)
# Domyślnie Nginx serwuje na porcie 80

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
