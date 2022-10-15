FROM node:latest

WORKDIR /docs

COPY . .

RUN yarn

RUN yarn build

CMD [ "yarn", "serve" ]