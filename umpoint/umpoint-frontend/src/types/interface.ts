export interface IFunction<T = any> {
    (x?: any): T;
}

export interface IObject<T = any> {
    [key: string]: T;
}

export interface IHttpResponse {
    code: number;
    msg: string;
    data: any;
}

export interface VueCalEvent {
    start: Date,
    end: Date,
    title?: String,
    content?: String,
    class?: String, // space-separated css classes.
    background?: Boolean, // Event type is background (non-editable), not CSS property
    split?: Number | String,
    allDay?: Boolean,
    deletable?: Boolean, // force undeletable when events are editable.
    resizable?: Boolean,
}