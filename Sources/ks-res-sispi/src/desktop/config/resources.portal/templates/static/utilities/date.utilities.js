export function FormatDateAndTime(dateString){
    /**
     * output: 15/02/2021 12:50:13
     */
    if(dateString){
        const d = new Date(dateString);
        const YY = '' + d.getFullYear();
        let mm = '' + (d.getMonth() + 1);
        let dd = '' + d.getDate();
        let hours = '' + d.getHours();
        let minutes = '' + d.getMinutes();
        let seconds = '' + d.getSeconds();
        if(mm < 10){
            mm = '0' + mm;
        }
        if(dd < 10){
            dd = '0'+dd;
        }
        if(hours < 10){
            hours = '0' + hours;
        }
        if(minutes < 10){
            minutes = '0' + minutes;
        }
        if(seconds < 10){
            seconds = '0' + seconds;
        }
        const completeDate = dd + '/' + mm + '/' + YY + ' ' + hours + ':' + minutes + ':' + seconds;
        return completeDate;
    } else {
        return '';
    }
}

export function FormatDate(dateString){
    /**
     * output: 15/02/2021
     */
    if(dateString){
        const d = new Date(dateString);
        const YY = '' + d.getFullYear();
        let mm = '' + (d.getMonth() + 1);
        let dd = '' + d.getDate();
        if(mm < 10){
            mm = '0' + mm;
        }
        if(dd < 10){
            dd = '0'+dd;
        }
        const completeDate = dd + '/' + mm + '/' + YY;
        return completeDate;
    } else {
        return '';
    }
}

export function LongDateFormat(dateString){
    /**
     * output: 15 Febbraio 2021
     */
    if(dateString){
        const d = new Date(dateString);
        const YY = '' + d.getFullYear();
        let mm = '' + (d.getMonth() + 1);
        let dd = '' + d.getDate();
        if(mm < 10){
            mm = '0' + mm;
        }
        if(dd < 10){
            dd = '0'+dd;
        }
        const completeDate = dd + ' ' + getMonth(mm) + ' ' + YY;
        return completeDate;
    } else {
        return '';
    }
}

function getMonth(month){
    let m = '';
    switch (month) {
        case '01':
            m = 'Gennaio';
            break;
        case '02':
            m = 'Febbraio';
            break;
        case '03':
            m = 'Marzo';
            break;
        case '04':
            m = 'Aprile';
            break;
        case '05':
            m = 'Maggio';
            break;
        case '06':
            m = 'Giugno';
            break;
        case '07':
            m = 'Luglio';
            break;
        case '08':
            m = 'Agosto';
            break;
        case '09':
            m = 'Settembre';
            break;
        case '10':
            m = 'Ottobre';
            break;
        case '11':
            m = 'Novembre';
            break;
        case '12':
            m = 'Dicembre';
            break;
        default:
            m = m + month;
            break;
    }
    return m;
}

export function TimeFormat(dateString){
    /**
     * output: 12:50:13
     */
    if(dateString){
        const d = new Date(dateString);
        let hours = '' + d.getHours();
        let minutes = '' + d.getMinutes();
        let seconds = '' + d.getSeconds();
        if(hours < 10){
            hours = '0' + hours;
        }
        if(minutes < 10){
            minutes = '0' + minutes;
        }
        if(seconds < 10){
            seconds = '0' + seconds;
        }
        const completeDate = hours + ':' + minutes + ':' + seconds;
        return completeDate;
    } else {
        return '';
    }
}

export function DateAdapterForInput(date){
    const d = new Date(date);
    const YY = '' + d.getFullYear();
    let mm = '' + (d.getMonth() + 1);
    let dd = '' + d.getDate();
    if(mm < 10){
        mm = '0' + mm;
    }
    if(dd < 10){
        dd = '0'+dd;
    }
    const newDate = YY +  '-' + mm + '-' + dd;
    return newDate;
}

export function DatetimeLocalFormat(isoDate){
    /**
     * output: 2021-07-21T00:01
     */
    if(isoDate){
        const offsetDate = new Date(new Date(isoDate).getTime() - (new Date(isoDate).getTimezoneOffset() * 60000)).toISOString();
        const dtlDate = offsetDate.substr(0, offsetDate.lastIndexOf(':'));
        return dtlDate;
    } else {
        return '';
    }
}

