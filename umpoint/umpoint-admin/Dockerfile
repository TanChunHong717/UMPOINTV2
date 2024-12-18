FROM node:18-alpine
WORKDIR /app
COPY package.json package-lock.json /app/
RUN npm install
COPY . .

# Set build args as environment variables during the build process
# Put here to prevent arg affecting npm install cache
ARG VITE_APP_API="http://localhost:8080/api"
ENV VITE_APP_API=$VITE_APP_API

RUN npm run build

FROM nginx:alpine
COPY --from=0 /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
