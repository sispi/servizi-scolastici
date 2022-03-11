export class Response{
    constructor(status, data, error){
        this.status = status;
        this.data = data;
        this.error = error;
    }
}