const fsm = uni.getFileSystemManager();
const FILE_BASE_NAME = 'tmp_base64src'; //自定义文件名

function base64src(base64data, cb) {
    base64data = base64data.replace(/[\r\n]/g, '');
    const filePath = `${uni.env.USER_DATA_PATH}/${FILE_BASE_NAME}`;
    fsm.unlink({
        filePath: filePath,
        fail: (res) => {
            console.log(res);
        },
        complete: (res) => {
            const buffer = uni.base64ToArrayBuffer(base64data);
            fsm.writeFile({
                filePath: filePath,
                data: buffer,
                encoding: 'base64',
                success: (res) => {
                    cb(filePath);
                },
                fail: (res) => {
                    return new Error('ERROR_BASE64SRC_WRITE');
                }
            });
        }
    });
}
export { base64src };
