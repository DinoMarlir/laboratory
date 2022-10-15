FROM node:latest

WORKDIR /docs

COPY package.json ./

RUN yarn

EXPOSE 3000

CMD [ "yarn", "serve" ]