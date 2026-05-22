FROM nginx:alpine

COPY apps/frontend/dist/ /usr/share/nginx/html/

COPY services/frontend/nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]