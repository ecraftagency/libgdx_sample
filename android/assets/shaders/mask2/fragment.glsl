#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_mask;
void main(){
    vec4 mask = texture2D(u_mask, v_texCoords);
    vec4 color = v_color * texture2D(u_texture, v_texCoords);
    gl_FragColor = color;
}